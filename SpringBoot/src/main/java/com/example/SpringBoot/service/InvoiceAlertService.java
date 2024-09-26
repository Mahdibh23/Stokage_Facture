package com.example.SpringBoot.service;

import com.example.SpringBoot.model.Facture;
import com.example.SpringBoot.repository.FactureRepository;
import com.example.SpringBoot.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class InvoiceAlertService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceAlertService.class);

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 0 1,15 * ?")
    public void checkForDueInvoices() {
        logger.info("Début de la vérification des factures en retard");

        LocalDate now = LocalDate.now();
        List<Facture> dueInvoices = factureRepository.findByDateBefore(now);

        logger.info("Nombre de factures en retard trouvées : {}", dueInvoices.size());

        for (Facture facture : dueInvoices) {
            if (facture.getEntreprise().getTotalAmount().compareTo(BigDecimal.ZERO) > 0) {
                LocalDate invoiceDate = facture.getDate();
                long daysPastDue = now.toEpochDay() - invoiceDate.toEpochDay();

                if (daysPastDue > 15) { // vérifier les factures en retard de plus de 15 jours
                    String email = facture.getEntreprise().getEmail();
                    String subject = "Facture en retard";
                    String text = "Votre facture avec le numéro " + facture.getInvoiceNumber() +
                            " est en retard depuis plus de 15 jours. Veuillez effectuer le paiement dès que possible. " +
                            "Montant dû: " + facture.getAmount() + " Date d'échéance: " + facture.getDate();

                    try {
                        emailService.sendSimpleMessage(email, subject, text);
                        logger.info("Email envoyé à : {}", email);
                    } catch (Exception e) {
                        logger.error("Erreur lors de l'envoi de l'email à : {}", email, e);
                    }
                }

            }
}
        logger.info("Fin de la vérification des factures en retard");
    }
}
