import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IParent, Parent } from 'app/shared/model/parent.model';
import { ParentService } from './parent.service';

@Component({
  selector: 'jhi-parent-update',
  templateUrl: './parent-update.component.html'
})
export class ParentUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(protected parentService: ParentService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ parent }) => {
      this.updateForm(parent);
    });
  }

  updateForm(parent: IParent) {
    this.editForm.patchValue({
      id: parent.id,
      name: parent.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const parent = this.createFromForm();
    if (parent.id !== undefined) {
      this.subscribeToSaveResponse(this.parentService.update(parent));
    } else {
      this.subscribeToSaveResponse(this.parentService.create(parent));
    }
  }

  private createFromForm(): IParent {
    return {
      ...new Parent(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParent>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
