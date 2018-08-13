
import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'storemedia.dart';

typedef void TimeChangeHandler(Duration duration);
typedef void ErrorHandler(String message);

class ArtistFinder {

  static const MethodChannel _channel = const MethodChannel('music_finder');
  TimeChangeHandler durationHandler;
  TimeChangeHandler positionHandler;
  VoidCallback startHandler;
  VoidCallback completionHandler;
  ErrorHandler errorHandler;

  int audioDuration=0;
  int currentPosition=0;

  String state ;


  ArtistFinder() {
    _channel.setMethodCallHandler(platformCallHandler);

  }

  Future<dynamic> getArtists() => _channel.invokeMethod('shareArtist1');

  void setDurationHandler(Duration duration) {
  }


  void setPositionHandler(Duration duration) {
    currentPosition = duration.inMilliseconds;
  }


  void setStartHandler(VoidCallback callback) {
    startHandler = callback;
  }

  void setCompletionHandler(VoidCallback callback) {

  }

  void setErrorHandler(ErrorHandler handler) {
    errorHandler = handler;
  }

  static Future<String> get platformVersion =>
      _channel.invokeMethod('getPlatformVersion');

  static Future<dynamic> allSongs() async {
    var completer = new Completer();


    Map params = <String, dynamic>{
      "handlePermissions": true,
      "executeAfterPermissionGranted": true,
    };
    List<dynamic> artists = await _channel.invokeMethod('getArtists', params);
    var mySongs = artists.map((m) => new Artist.fromMap(m)).toList();
    completer.complete(mySongs);
    return completer.future;
  }

  Future platformCallHandler(MethodCall call) async {
    switch (call.method) {
      case "onFftVisualization":
        break;
      case "onWaveformVisualization":
        break;
      case "audio.onDuration":
        final duration = new Duration(milliseconds: call.arguments);
        if (durationHandler != null) {
          durationHandler(duration);
        }

        audioDuration=call.arguments;

        break;
      case "audio.onCurrentPosition":

        final duration = new Duration(milliseconds: call.arguments);
        if (positionHandler != null) {
          positionHandler(new Duration(milliseconds: call.arguments));
        }

        currentPosition=call.arguments;


        break;
      case "audio.onStart":
        if (startHandler != null) {
          startHandler();
        }
        break;
      case "audio.onComplete":
        if (completionHandler != null) {
        }

        break;
      case "shareDataArtist":
        String list=call.arguments.toString();
        setArtist(list.split(', ')).then((bool committed){

        });

        setArtistCount(list.split(', ').length).then((bool committed){});
        break;
      case "audio.onError":
        if (errorHandler != null) {
          errorHandler(call.arguments);
        }
        break;
      default:

    }
  }
}

class Artist {

  int id;

  String name;

  int nbreAlbum;

  int nbreTracks;

  String albumArt;



  Artist(this.id,
      this.name ,
      this.nbreAlbum,
      this.nbreTracks,
      this.albumArt);
  Artist.fromMap(Map m) {
    id = m["id"];
    name = m["name"];
    nbreAlbum = m["nbreAlbum"];
    nbreTracks = m["nbreTracks"];
    albumArt = m["albumArt"];
  }
}