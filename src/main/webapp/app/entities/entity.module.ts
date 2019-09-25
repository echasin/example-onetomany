import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'parent',
        loadChildren: () => import('./parent/parent.module').then(m => m.JhipsterParentModule)
      },
      {
        path: 'child',
        loadChildren: () => import('./child/child.module').then(m => m.JhipsterChildModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class JhipsterEntityModule {}
