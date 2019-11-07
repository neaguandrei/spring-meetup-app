import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { AcademyProjectSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { SwitchComponent } from 'app/shared/switch/switch.component';

@NgModule({
  imports: [AcademyProjectSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, SwitchComponent],
  entryComponents: [JhiLoginModalComponent, SwitchComponent],
  exports: [AcademyProjectSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, SwitchComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AcademyProjectSharedModule {
  static forRoot() {
    return {
      ngModule: AcademyProjectSharedModule
    };
  }
}
