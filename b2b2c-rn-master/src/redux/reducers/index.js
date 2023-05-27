/**
 * Created by Andste on 2018/9/29.
 */
import { combineReducers } from 'redux'

import user from './user'
import message from './message'
import token from './token'
import search from './search'
import cart from './cart'

const rootReducer = combineReducers({
  user,
  message,
  token,
  search,
  cart
})

export default rootReducer
