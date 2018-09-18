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
        console.log('contact type: ' + 1);
        console.log('cordovaService:', this.cordovaService);
        console.log('getCordova:', this.cordovaService.getCordova());
        //
        const cordova = this.cordovaService.getCordova();
        const successCallback = function(res) {
          console.log('inside successCallback:', res);
        };
        const errorCallback = function(error) {
          console.log('inside errorCallback:', error);
        };
        try {
          cordova.exec(successCallback, errorCallback, 'ContactPlugin', 'getContacts', []);
        }catch(e){
          errorCallback(e);
        }
        //
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
