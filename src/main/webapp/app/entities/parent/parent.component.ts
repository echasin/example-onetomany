import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IParent } from 'app/shared/model/parent.model';
import { AccountService } from 'app/core/auth/account.service';
import { ParentService } from './parent.service';

@Component({
  selector: 'jhi-parent',
  templateUrl: './parent.component.html'
})
export class ParentComponent implements OnInit, OnDestroy {
  parents: IParent[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected parentService: ParentService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.parentService
      .query()
      .pipe(
        filter((res: HttpResponse<IParent[]>) => res.ok),
        map((res: HttpResponse<IParent[]>) => res.body)
      )
      .subscribe(
        (res: IParent[]) => {
          this.parents = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInParents();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IParent) {
    return item.id;
  }

  registerChangeInParents() {
    this.eventSubscriber = this.eventManager.subscribe('parentListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
