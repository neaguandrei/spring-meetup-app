import { NgModule } from '@angular/core';

import { AcademyProjectSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [AcademyProjectSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [AcademyProjectSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class AcademyProjectSharedCommonModule {}
