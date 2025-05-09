export interface Page<T> {
  content: T[]; // Lista de elementos en la página actual
  totalElements: number; // Total de elementos en todas las páginas
  totalPages: number; // Total de páginas
  size: number; // Tamaño de la página
  number: number; // Número de la página actual (0-indexed)
  first: boolean; // Si es la primera página
  last: boolean; // Si es la última página
  empty: boolean; // Si la página está vacía
}
