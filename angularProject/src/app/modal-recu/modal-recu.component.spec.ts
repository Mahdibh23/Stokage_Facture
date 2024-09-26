import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalRecuComponent } from './modal-recu.component';

describe('ModalRecuComponent', () => {
  let component: ModalRecuComponent;
  let fixture: ComponentFixture<ModalRecuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalRecuComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalRecuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
