from flask import Flask, request, jsonify
from flask_cors import CORS
import pytesseract
from PIL import Image, ImageEnhance, ImageFilter, ImageOps
import re
import fitz  # PyMuPDF
import concurrent.futures

app = Flask(__name__)
CORS(app)

def preprocess_image(image):
    img = image.convert('L')  # Convert to grayscale
    img = ImageOps.autocontrast(img)  # Apply autocontrast
    enhancer = ImageEnhance.Contrast(img)
    img = enhancer.enhance(2)  # Increase contrast
    img = img.filter(ImageFilter.MedianFilter())  # Apply median filter
    img = img.filter(ImageFilter.SHARPEN)  # Enhance sharpness
    img = img.filter(ImageFilter.EDGE_ENHANCE)  # Enhance edges
    img = img.resize((img.width, img.height))  # Resize to original size
    return img

def clean_text(text):
    # Remove unwanted special characters but keep /, -, _, and .
    text = re.sub(r'[^a-zA-Z0-9\s\/\-_.]', '', text)
    # Remove single letters that are not meaningful
    words = text.split()
    cleaned_words = [word for word in words if len(word) > 1 or word.lower() in ('a', 'à', 'y')]
    cleaned_text = ' '.join(cleaned_words)
    return cleaned_text

def extract_text_from_image(image):
    img = preprocess_image(image)
    custom_config = r'--oem 3 --psm 6'  # Using LSTM OCR engine with automatic page segmentation mode
    text = pytesseract.image_to_string(img, lang='fra', config=custom_config)
    text = clean_text(text)  # Clean the extracted text
    return text

def extract_invoice_details(text):
    invoice_number_pattern = r"Facture\s*(\d+)"
    objet_pattern = r"Objet\s+(.+?)(?:\n|Contrat)"
    date_pattern = r"Date\s*(\d{2}/\d{2}/\d{4})"
    amount_pattern = r"somme\s*(.*)\s*Millimes"

    invoice_number = re.search(invoice_number_pattern, text)
    objet = re.search(objet_pattern, text)
    date = re.search(date_pattern, text)
    amount = re.search(amount_pattern, text)

    invoice_number = invoice_number.group(1) if invoice_number else None
    objet = objet.group(1) if objet else None
    date = date.group(1) if date else None
    amount = amount.group(1) if amount else None

    return invoice_number, date, amount, objet

numbers = {
    "zéro": 0, "un": 1, "deux": 2, "trois": 3, "quatre": 4, "cinq": 5, "six": 6, "sept": 7, "huit": 8, "neuf": 9,
    "dix": 10, "onze": 11, "douze": 12, "treize": 13, "quatorze": 14, "quinze": 15, "seize": 16, 
    "vingt": 20, "trente": 30, "quarante": 40, "cinquante": 50, "soixante": 60,
    "soixante-dix": 70, "soixante et onze": 71, "soixante-douze": 72, "soixante-treize": 73, "soixante-quatorze": 74,
    "soixante-quinze": 75, "soixante-seize": 76, "soixante-dix-sept": 77, "soixante-dix-huit": 78, "soixante-dix-neuf": 79,
    "quatre-vingt": 80, "quatre-vingt-un": 81, "quatre-vingt-deux": 82, "quatre-vingt-trois": 83, "quatre-vingt-quatre": 84,
    "quatre-vingt-cinq": 85, "quatre-vingt-six": 86, "quatre-vingt-sept": 87, "quatre-vingt-huit": 88, "quatre-vingt-neuf": 89,
    "quatre-vingt-dix": 90, "quatre-vingt-onze": 91, "quatre-vingt-douze": 92, "quatre-vingt-treize": 93, "quatre-vingt-quatorze": 94,
    "quatre-vingt-quinze": 95, "quatre-vingt-seize": 96, "quatre-vingt-dix-sept": 97, "quatre-vingt-dix-huit": 98, "quatre-vingt-dix-neuf": 99,
    "cent": 100, "mille": 1000
}

def parse_compound_numbers(text):
    text = text.replace("quatre_vingt", "quatre-vingt").replace("soixante et onze", "soixante-et-onze")
    return text

def replace_underscores_with_hyphens(text):
    text = text.replace('_', '-')
    text = re.sub(r'\s*-\s*', '-', text)
    return text

def text_to_number(text):
    text = parse_compound_numbers(text)
    words = text.split()
    total = 0
    current = 0
    for word in words:
        if word in numbers:
            scale = numbers[word]
            if scale == 100:
                if current == 0:
                    current = 1
                current *= scale
            else:
                current += scale
        elif word == "mille":
            total += current * 1000
            current = 0
    total += current
    return total

def convert_text_to_number(text):
    text = text.lower()
    text = replace_underscores_with_hyphens(text)
    text = re.sub(r'[^a-z\s-]', '', text)
    parts = text.split("dinars")
    if len(parts) != 2:
        raise ValueError("Le texte doit contenir 'Dinars' comme séparateur entre les unités et les millimes.")
    
    dinars_part = parts[0].strip()
    millimes_part = parts[1].strip().replace("millimes", "").strip()
    
    dinars = text_to_number(dinars_part)
    millimes = text_to_number(millimes_part)
    
    return f"{dinars}.{millimes:03d}"

def extract_text_from_pdf_page(page):
    pix = page.get_pixmap()
    image = Image.frombytes("RGB", [pix.width, pix.height], pix.samples)
    return extract_text_from_image(image)

@app.route('/process_invoice', methods=['POST'])
def process_invoice():
    file = request.files['facture']
    if not file:
        return jsonify({"error": "No file uploaded"}), 400

    file_ext = file.filename.split('.')[-1].lower()
    text = ""
    
    if file_ext in ['jpg', 'jpeg', 'png']:
        image = Image.open(file.stream)
        text = extract_text_from_image(image)
    elif file_ext == 'pdf':
        file_bytes = file.read()
        pdf_document = fitz.open("pdf", file_bytes)
        
        with concurrent.futures.ThreadPoolExecutor() as executor:
            futures = [executor.submit(extract_text_from_pdf_page, pdf_document.load_page(page_num)) for page_num in range(len(pdf_document))]
            results = [future.result() for future in concurrent.futures.as_completed(futures)]
        
        text = "\n".join(results)
        
    else:
        return jsonify({"error": "Unsupported file type"}), 400
    
    print('text : ', text)
    invoice_number, date, amount, objet = extract_invoice_details(text)

    if amount:
        try:
            amount_number = convert_text_to_number(amount)
        except ValueError as e:
            return jsonify({"error": str(e)}), 400
    else:
        amount_number = None

    return jsonify({
        "invoice_number": invoice_number,
        "date": date,
        "amount_text": amount,
        "amount_number": amount_number,
        "objet": objet
    })

@app.route('/process_recu', methods=['POST'])
def process_recu():
    file = request.files['file']
    if not file:
        return jsonify({"error": "No file uploaded"}), 400

    file_ext = file.filename.split('.')[-1].lower()
    text = ""
    
    if file_ext in ['jpg', 'jpeg', 'png']:
        image = Image.open(file.stream)
        text = extract_text_from_image(image)
    elif file_ext == 'pdf':
        file_bytes = file.read()
        pdf_document = fitz.open("pdf", file_bytes)
        
        with concurrent.futures.ThreadPoolExecutor() as executor:
            futures = [executor.submit(extract_text_from_pdf_page, pdf_document.load_page(page_num)) for page_num in range(len(pdf_document))]
            results = [future.result() for future in concurrent.futures.as_completed(futures)]
        
        text = "\n".join(results)
        
    else:
        return jsonify({"error": "Unsupported file type"}), 400
    
    print(text)
    return jsonify({"text": text})

if __name__ == '__main__':
    app.run(debug=True)
