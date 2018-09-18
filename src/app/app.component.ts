import { Component, OnInit } from '@angular/core';
import { CordovaService } from './cordova.service';
declare var device;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Viet';
  constructor(private cordovaService: CordovaService) {

  }

  ngOnInit() {
    // document.addEventListener('deviceready', () => {
    //   alert(device.platform);
    // }, false);
  }

  readContact = (num) => {
    switch(num) {
      case 1: {
        alert('contact type: ' + 1);
        console.log('cordovaService:', this.cordovaService);
        console.log('getCordova:', this.cordovaService.getCordova());
        // window.plugins.contactPlugin.show('getContacts', function() {
        //   console.log('SUCCESS');
        // }, function(error){
        //   console.log('ERROR:', error);
        // });
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
