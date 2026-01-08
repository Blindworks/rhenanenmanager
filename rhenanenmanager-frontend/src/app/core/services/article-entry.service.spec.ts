import { TestBed } from '@angular/core/testing';

import { ArticleEntryService } from './article-entry.service';

describe('ArticleEntryService', () => {
  let service: ArticleEntryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArticleEntryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
