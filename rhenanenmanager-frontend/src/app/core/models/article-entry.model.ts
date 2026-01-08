/**
 * Model representing an article entry from the Rhenanenruf (Corps journal).
 */
export interface ArticleEntry {
  id: number;
  title: string;
  subtitle?: string;
  alternativeAuthor?: string;
  category?: string;
  text?: string;
  year?: number;
  month?: number;
  page?: number;
  date?: string;
  created?: string;
  createdBy?: string;
  modified?: string;
  modifiedBy?: string;
}

/**
 * Paginated response for article entries.
 */
export interface ArticleEntryPage {
  content: ArticleEntry[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

/**
 * Helper function to get formatted issue string (e.g., "Ausgabe 01/2024").
 */
export function getFormattedIssue(article: ArticleEntry): string {
  if (article.month && article.year) {
    return `Ausgabe ${String(article.month).padStart(2, '0')}/${article.year}`;
  } else if (article.year) {
    return `Ausgabe ${article.year}`;
  }
  return 'Ausgabe unbekannt';
}

/**
 * Helper function to get text preview (first 200 characters).
 */
export function getTextPreview(article: ArticleEntry): string {
  if (!article.text) {
    return '';
  }
  return article.text.length > 200 ? article.text.substring(0, 200) + '...' : article.text;
}
