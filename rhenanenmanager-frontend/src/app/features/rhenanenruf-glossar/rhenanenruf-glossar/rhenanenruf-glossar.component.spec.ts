import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RhenanenrufGlossarComponent } from './rhenanenruf-glossar.component';

describe('RhenanenrufGlossarComponent', () => {
  let component: RhenanenrufGlossarComponent;
  let fixture: ComponentFixture<RhenanenrufGlossarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RhenanenrufGlossarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RhenanenrufGlossarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
