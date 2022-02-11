import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { homeRoutes } from './home.route';
import { HomeComponent } from './home.component';
import {NgxSliderModule} from "@angular-slider/ngx-slider";

@NgModule({
  imports: [SharedModule, RouterModule.forChild([homeRoutes[0],homeRoutes[1]]), NgxSliderModule],
  declarations: [HomeComponent],
})
export class HomeModule {}
