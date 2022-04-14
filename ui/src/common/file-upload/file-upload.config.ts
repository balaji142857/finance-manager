export interface FileUploadConfig {
	permissions: string[];
	numFiles: number;
	maxFileSize: number;
	netSize: number;
	fileTypes: string;
	permission: {
		upload: string;
		download: string;
		delete: string
	}
}
