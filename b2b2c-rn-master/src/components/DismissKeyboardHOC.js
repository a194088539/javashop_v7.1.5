/**
 * Created by Andste on 2017/8/5.
 * https://stackoverflow.com/questions/29685421/react-native-hide-keyboard
 * @Eric Kim
 */
import React from 'react'
import {
  TouchableWithoutFeedback,
  Keyboard
} from 'react-native'

const DismissKeyboardHOC = (Comp) => {
  return ({children, ...props}) => (
    <TouchableWithoutFeedback onPress={ Keyboard.dismiss }>
      <Comp { ...props }>
        { children }
      </Comp>
    </TouchableWithoutFeedback>
  )
}

export default DismissKeyboardHOC
