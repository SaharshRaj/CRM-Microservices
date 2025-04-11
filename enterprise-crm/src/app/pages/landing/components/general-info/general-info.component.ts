import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { selectCurrentUser } from '../../../../store/index.selectors';
import { Employee } from '../../../../models/Employee';

@Component({
  selector: 'app-general-info',
  standalone: false,

  templateUrl: './general-info.component.html',
  styleUrl: './general-info.component.scss'
})
export class GeneralInfoComponent {

    user!: Employee | null;

    constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.select(selectCurrentUser).subscribe((user) => {
      this.user = user;
    });
}


    notificationList = [
        {
          title: "New message from John Doe",
          description: "Hey, how are you doing?",
          time: "2 hours ago",
          read: false // true or false
        },
        {
          title: "New comment on your post",
          description: "Great post! I really enjoyed it.",
          time: "3 hours ago",
          read: true
        },
        {
          title: "New follower",
          description: "Jane Smith started following you.",
          time: "1 day ago",
          read: false
        },
        {
          title: "System update available",
          description: "A new version of the app is available for download.",
          time: "2 days ago",
          read: true
        },
        {
          title: "New task assigned",
          description: "You have been assigned a new task in the project.",
          time: "3 days ago",
          read: false
        },
        {
          title: "New event invitation",
          description: "You have been invited to a new event.",
          time: "4 days ago",
          read: true
        },
        {
          title: "Password change",
          description: "Your password has been changed successfully.",
          time: "5 days ago",
          read: false
        },
        {
          title: "New feature release",
          description: "Check out the new features we just released!",
          time: "6 days ago",
          read: true
        },
        {
          title: "Weekly summary",
          description: "Here is your weekly summary of activities.",
          time: "7 days ago",
          read: false
        },
        {
          title: "Feedback request",
          description: "We would love to hear your feedback on our service.",
          time: "8 days ago",
          read: true
        }
      ].slice(0, 3); // Limit to 5 notifications

}
