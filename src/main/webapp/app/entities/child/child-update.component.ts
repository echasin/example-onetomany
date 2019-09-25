import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IChild, Child } from 'app/shared/model/child.model';
import { ChildService } from './child.service';
import { IParent } from 'app/shared/model/parent.model';
import { ParentService } from 'app/entities/parent/parent.service';

@Component({
  selector: 'jhi-child-update',
  templateUrl: './child-update.component.html'
})
export class ChildUpdateComponent implements OnInit {
  isSaving: boolean;

  parents: IParent[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    parent: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected childService: ChildService,
    protected parentService: ParentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ child }) => {
      this.updateForm(child);
    });
    this.parentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IParent[]>) => mayBeOk.ok),
        map((response: HttpResponse<IParent[]>) => response.body)
      )
      .subscribe((res: IParent[]) => (this.parents = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(child: IChild) {
    this.editForm.patchValue({
      id: child.id,
      name: child.name,
      parent: child.parent
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const child = this.createFromForm();
    if (child.id !== undefined) {
      this.subscribeToSaveResponse(this.childService.update(child));
    } else {
      this.subscribeToSaveResponse(this.childService.create(child));
    }
  }

  private createFromForm(): IChild {
    return {
      ...new Child(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      parent: this.editForm.get(['parent']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChild>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackParentById(index: number, item: IParent) {
    return item.id;
  }
}
