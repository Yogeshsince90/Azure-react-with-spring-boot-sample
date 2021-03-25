import axios from 'axios';
import './App.css';
import { useState } from 'react';
// import {authProvider} from './auth/authProvider'
function App() {
  
  const [data, setData] = useState({showData: '', writeData:'',commonData:''});

  const showDataHandler1 = () => {
    debugger;
    //const token = await authProvider.getAccessToken();
    
    axios.get('http://localhost:8080/api/writeData')
    .then(res=>
      res.data
    )
    .then(resp=>{
      debugger;
      setData({...data, writeData: resp})
    })
    .catch(error => {
      console.log(error);
      debugger;
      setData({...data,writeData: 'Error: Unauthorized'})
    })
    
  }
  const showDataHandler2 = () => {
    debugger;
    //const token = await authProvider.getAccessToken();
    
    axios.get('http://localhost:8080/api/showData')
    .then(res=>
      res.data
    )
    .then(resp=>{
      debugger;
      setData({...data, showData: resp})
    })
    .catch(error => {
      setData({...data,showData: 'Error: Unauthorized'})
    })
    
  }
  const showDataHandler3 = () => {
    debugger;
    //const token = await authProvider.getAccessToken();
    
    axios.get('http://localhost:8080/api/viewCommonData')
    .then(res=>
      res.data
    )
    .then(resp=>{
      debugger;
      setData({...data, commonData: resp})
    })
    .catch(error => {
      debugger
      setData({...data,commonData: 'Error: Unauthorized'})
    })
    
  }
  return (
        <div className="App">
      <button type="button" onClick={showDataHandler1} name="showData">SHOW DATA FOR UI_WRITE ROLE</button>
      <div style={{minWidth:'50px',minHeight:'50px', backgroundColor: 'yellow'}}><strong >{data.writeData}</strong></div>
      <div style={{minWidth:'50px', minHeight:'50px'}}></div>
      <button type="button" onClick={showDataHandler2} name="showData">SHOW DATA FOR UI_READ ROLE</button>
      <div style={{minWidth:'50px',minHeight:'50px', backgroundColor: 'yellow'}}><strong >{data.showData}</strong></div>
      <div style={{minWidth:'50px', minHeight:'50px'}}></div>

      <button type="button" onClick={showDataHandler3} name="showData">SHOW DATA COMMON FOR BOTH ROLE</button>
      <div style={{minWidth:'50px',minHeight:'50px', backgroundColor: 'yellow'}}><strong >{data.commonData}</strong></div>
      <div style={{minWidth:'50px', minHeight:'50px'}}></div>
    </div>
  );
}

export default App;
