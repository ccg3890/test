import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpecDetailComponent } from './spec-detail.component';

describe('Spec Management Detail Component', () => {
  let comp: SpecDetailComponent;
  let fixture: ComponentFixture<SpecDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SpecDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ spec: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(SpecDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SpecDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load spec on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.spec).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
