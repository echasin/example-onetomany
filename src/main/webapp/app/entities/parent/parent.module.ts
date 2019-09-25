import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSharedModule } from 'app/shared/shared.module';
import { ParentComponent } from './parent.component';
import { ParentDetailComponent } from './parent-detail.component';
import { ParentUpdateComponent } from './parent-update.component';
import { ParentDeletePopupComponent, ParentDeleteDialogComponent } from './parent-delete-dialog.component';
import { parentRoute, parentPopupRoute } from './parent.route';

const ENTITY_STATES = [...parentRoute, ...parentPopupRoute];

@NgModule({
  imports: [JhipsterSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ParentComponent, ParentDetailComponent, ParentUpdateComponent, ParentDeleteDialogComponent, ParentDeletePopupComponent],
  entryComponents: [ParentDeleteDialogComponent]
})
export class JhipsterParentModule {}
