import axios from 'axios';
import './App.css';
import { useState } from 'react';
import {authProvider} from './auth/authProvider'
function App() {
  
  const [data, setData] = useState({showData: '', writeData:''});

  const showDataHandler = async () => {
    debugger;
    const token = await authProvider.getAccessToken();
    let config = {
      headers: {Authorization : 'Bearer '+ token.accessToken}
      }

    axios.post('http://localhost:8080/api/showData',null,config)
    .then(res=>
      res.text
    )
    .then(resp=>{
      debugger;
      setData({...data, showData: resp})
    })
    .catch(console.log)
    
  }
  return (
        <div className="App">
      <button type="button" onClick={showDataHandler} name="showData">SHOW DATA</button>
      <div style={{minWidth:'50px',minHeight:'50px', backgroundColor: 'yellow'}}><strong >{data.showData}</strong></div>
      <div style={{minWidth:'50px', minHeight:'50px'}}></div>
    </div>
  );
}

export default App;
