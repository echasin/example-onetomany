import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSharedModule } from 'app/shared/shared.module';
import { ChildComponent } from './child.component';
import { ChildDetailComponent } from './child-detail.component';
import { ChildUpdateComponent } from './child-update.component';
import { ChildDeletePopupComponent, ChildDeleteDialogComponent } from './child-delete-dialog.component';
import { childRoute, childPopupRoute } from './child.route';

const ENTITY_STATES = [...childRoute, ...childPopupRoute];

@NgModule({
  imports: [JhipsterSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ChildComponent, ChildDetailComponent, ChildUpdateComponent, ChildDeleteDialogComponent, ChildDeletePopupComponent],
  entryComponents: [ChildDeleteDialogComponent]
})
export class JhipsterChildModule {}
