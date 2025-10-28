import { Component } from '@angular/core';
import { AddManagerComponent } from '../add-manager/add-manager.component';
import { AddAgentComponent } from '../add-agent/add-agent.component';
import { CommonModule } from '@angular/common';
import { NavBarComponent } from '../../navigation/navigation.component';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [AddManagerComponent,AddAgentComponent,CommonModule,NavBarComponent],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {

  selectedForm: 'manager' | 'agent' | null = null;
  showForm(form: 'manager' | 'agent') {
    this.selectedForm = form;
  }
}
