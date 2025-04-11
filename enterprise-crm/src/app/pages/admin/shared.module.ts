import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { AppFloatingConfigurator } from '../layout/component/app.floatingconfigurator';
import { RippleModule } from 'primeng/ripple';
import { RouterModule } from '@angular/router';
import { CheckboxModule } from 'primeng/checkbox';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { TabsModule } from 'primeng/tabs';
import { AvatarModule } from 'primeng/avatar';
import { AvatarGroupModule } from 'primeng/avatargroup';
import { Menu } from 'primeng/menu';
import { Dialog } from 'primeng/dialog';
import { SelectButton } from 'primeng/selectbutton';
import { ConfirmDialog } from 'primeng/confirmdialog';

@NgModule({
    imports: [CommonModule, ButtonModule, InputTextModule, TabsModule, RouterModule, RippleModule, AppFloatingConfigurator, ButtonModule, CheckboxModule, FormsModule, AvatarModule, AvatarGroupModule, Menu, Dialog, SelectButton, ConfirmDialog, FormsModule],
    exports: [CommonModule, ButtonModule, InputTextModule, TabsModule, RouterModule, RippleModule, AppFloatingConfigurator, ButtonModule, CheckboxModule, FormsModule, AvatarModule, AvatarGroupModule, Menu, Dialog, SelectButton, ConfirmDialog, FormsModule],
})
export class SharedModule {}
