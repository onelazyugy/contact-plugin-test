import { Component, OnInit } from '@angular/core';
declare var device;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Viet';

  ngOnInit() {
    document.addEventListener('deviceready', () => {
      alert(device.platform);
    }, false);
  }

  readContact = (num) => {
    switch(num) {
      case 1: {
        alert('contact type: ' + 1);
        break;
      }
      case 2: {
        alert('contact type: ' + 2);
        break;
      }
    }
  }

  writeExtStorage = (num) => {
    switch(num) {
      case 1: {
        alert('storage type: ' + 1);
        break;
      }
      case 2: {
        alert('storage type: ' + 2);
        break;
      }
    }
  }
}
