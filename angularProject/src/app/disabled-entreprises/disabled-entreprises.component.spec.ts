import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisabledEntreprisesComponent } from './disabled-entreprises.component';

describe('DisabledEntreprisesComponent', () => {
  let component: DisabledEntreprisesComponent;
  let fixture: ComponentFixture<DisabledEntreprisesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisabledEntreprisesComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DisabledEntreprisesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
