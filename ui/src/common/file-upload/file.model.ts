export interface FileModel {
	id?: number;
	name: string;
	size: number;
	content?: File;
	section: string;
  	format?: null;
  	file?: File
}
