/**
 * Created by Andste on 2018/9/29.
 */
import React from 'react'
import { Provider } from 'react-redux'
import { PersistGate } from 'redux-persist/integration/react'
import { persistor, store } from './redux/store'

import AppNavigator from './navigator'

const App = () => {
  return (
    <Provider store={ store }>
      <PersistGate loading={ null } persistor={ persistor }>
        <AppNavigator/>
      </PersistGate>
    </Provider>
  )
}

export default App
