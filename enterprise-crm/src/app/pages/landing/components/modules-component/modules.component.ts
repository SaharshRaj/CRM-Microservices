import { Component } from '@angular/core';

@Component({
  selector: 'app-modules',
  templateUrl: './modules.component.html',
  styleUrls: ['./modules.component.scss'],
  standalone: false
})
export class ModulesComponent {

  modules = [
    {
      title: "Customers",
      description: "Manage customer profiles and interactions",
      icon: "users",
      href: "/pages/services/customer-data-management",
      stats: "1,248 total",
      color: "bg-blue-100 text-blue-700",
    },
    {
      title: "Sales",
      description: "Track deals and revenue pipeline",
      icon: "dollar",
      href: "/pages/services/sales-automation",
      stats: "$1.2M pipeline",
      color: "bg-green-100 text-green-700",
    },
    {
      title: "Support",
      description: "Handle customer support tickets",
      icon: "phone",
      href: "/pages/services/customer-support",
      stats: "24 open tickets",
      color: "bg-amber-100 text-amber-700",
    },
    {
      title: "Marketing",
      description: "Create and monitor campaigns",
      icon: "megaphone",
      href: "/pages/services/marketing-automation",
      stats: "8 active campaigns",
      color: "bg-purple-100 text-purple-700",
    },
    {
      title: "Analytics",
      description: "View business performance metrics",
      icon: "chart-bar",
      href: "/pages/services/analytics-and-reporting",
      stats: "Updated today",
      color: "bg-indigo-100 text-indigo-700",
    }
  ]

}
