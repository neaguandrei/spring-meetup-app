import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'jhi-switch',
  templateUrl: './switch.component.html',
  styleUrls: ['./switch.component.scss']
})
export class SwitchComponent implements OnInit {
  checked = false;
  @Output() onCheck = new EventEmitter();
  @Input() labelMessage = '';

  constructor() {}

  ngOnInit() {}

  check() {
    this.checked = !this.checked;
    this.onCheck.emit({ checked: this.checked });
  }
}
