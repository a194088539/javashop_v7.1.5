/**
 * Created by Andste on 2018/9/29.
 */
import { AsyncStorage } from 'react-native'
import { applyMiddleware, createStore, compose } from 'redux'
import thunk from 'redux-thunk'
import logger from 'redux-logger'
import { persistStore, persistReducer } from 'redux-persist'
import rootReducer from '../reducers'

const persistConfig = {
  key: 'root',
  storage: AsyncStorage,
  whitelist: ['user', 'token']
}

const persistedReducer = persistReducer(persistConfig, rootReducer)
const store = createStore(persistedReducer, compose(applyMiddleware(thunk, logger)))
const persistor = persistStore(store)

export {
  persistor,
  store
}
