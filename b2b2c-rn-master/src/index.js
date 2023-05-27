/**
 * Created by Andste on 2018/9/29.
 */
import { AppRegistry } from 'react-native';
import App from './App';
import { name } from '../app.json';

if (!__DEV__) {
  global.console = {
    info : () => {},
    log  : () => {},
    warn : () => {},
    error: () => {}
  };
}
// console.ignoredYellowBox = ['Warning: BackAndroid is deprecated. Please use BackHandler instead.','source.uri should not be an empty string','Invalid props.style key']
// console.disableYellowBox = true

AppRegistry.registerComponent(name, () => App);
