import { Component, OnInit, forwardRef, Input } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { FileModel } from './file.model';

@Component({
	selector: 'app-file-upload',
	templateUrl: './file-upload.component.html',
	styleUrls: ['./file-upload.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => FileUploadComponent),
			multi: true
		}
	]
})
export class FileUploadComponent implements OnInit, ControlValueAccessor {

	isDisabled: boolean;
	propagateChange: (x: any) => {};
	files: FileModel[] = [];
	@Input() formats;
	config= {
		section: 'DEFAULT_SECTION'
	}
	Math = Math;
	constructor() { }

	writeValue(obj: FileModel[]): void {
		this.files = obj;
	}

	registerOnChange(fn: any): void {
		this.propagateChange = fn;
	}

	registerOnTouched(fn: any): void { }

	setDisabledState?(isDisabled: boolean): void {
		this.isDisabled = isDisabled;
	}

	ngOnInit() {

	}

	allowDrop(event) {
		this.stopEvent(event);
	}

	drop(event) {
		this.handleUpload(event.dataTransfer.files);
		this.stopEvent(event);
	}

	filesSelected(event) {
		console.log('filesAdded', event);
		this.files.push(...this.toModel(event.target.files));
	}

	handleUpload(files: File[]) {
		// validate file types
	 this.files.push(...this.toModel(files));
	 this.propagateChange(this.files);
	}

	toModel(values: File[]): FileModel[] {
		const returnVal: FileModel[] = [];
		if (values && values.length) {
				for (let i =0 ; i <values.length; i++) {
					const val = values[i];
						returnVal.push({
							content: val,
							name: val.name,
							size: val.size,
							format: null,
							section: this.config.section,
						});
				};
		}
		return returnVal;
	}

	download(file: File, index: number, event: any) {
		event.stopPropagation();
		event.preventDefault();
	}

	delete(file: File, index: number, event: any) {
		this.files.splice(index, 1);
		event.stopPropagation();
		event.preventDefault();
	}

	stopEvent(event) {
		event.preventDefault();
	}
}
