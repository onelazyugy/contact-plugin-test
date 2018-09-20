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
  permissionGranted = false;
  constructor(private cordovaService: CordovaService) {}

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
        const cordova = this.cordovaService.getCordova();
        try {
          cordova.exec(
            (res)=>{
              console.log('inside successCallback:', res);
              const responseJSON = JSON.parse(res);
              console.log('responseJSON:', responseJSON);
              this.permissionGranted = responseJSON.isUserGrantedPermission;
              console.log('this.permissionGranted:', this.permissionGranted)
            }, 
            (error)=>{
              console.log('inside errorCallback:', error);
            }, 'ContactPlugin', 'getContacts', []);
        }catch(e){
          console.log('error:', e);
        }
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
