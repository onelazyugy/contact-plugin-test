import { Injectable, NgZone } from '@angular/core';
// import { Subject } from 'rxjs/Subject';
import { BehaviorSubject, fromEvent, Observable } from 'rxjs';
// import { Observable } from 'rxjs';
// import 'rxjs/add/observable/fromEvent';
// import 'rxjs/add/operator/map';
// https://medium.com/@EliaPalme/how-to-wrap-an-angular-app-with-apache-cordova-909024a25d79
function _window(): any {
  // return the global native browser window object
  return window;
}
@Injectable()
export class CordovaService {
  private resume: BehaviorSubject<boolean>;
  constructor(private zone: NgZone) {
    this.resume = new BehaviorSubject<boolean>(null);
    fromEvent(document, 'resume').subscribe(event => {
      this.zone.run(() => {
        this.onResume();
      });
    });
  }

  getCordova(): any {
    return _window().cordova;
  }
  onCordova(): Boolean {
    return !!_window().cordova;
  }
  onResume(): void {
    this.resume.next(true);
  }
}