import { MsalAuthProvider, LoginType } from 'react-aad-msal';
 
// Msal Configurations
//d255ba17-2b8c-42a1-999c-8afcae7c72c5
const config = {
  auth: {
    authority: 'https://login.microsoftonline.com/d255ba17-2b8c-42a1-999c-8afcae7c72c5',
    clientId: 'c4acd9a6-9f63-4b37-a080-0b210504ac25',
    postLogoutRedirectUri: window.location.origin,
    validateAuthority: true,
    navigateToLoginRequestUrl: false,
  },
  cache: {
    cacheLocation: "localStorage",
    storeAuthStateInCookie: true
  }
};
 
// Authentication Parameters
const authenticationParameters = {
  scopes: [
    'User.Read'
  ]
}
 
// Options
const options = {
  loginType: LoginType.Redirect,
  tokenRefreshUri: window.location.origin 
}
 
export const authProvider = new MsalAuthProvider(config, authenticationParameters, options);
export const authenticateRequest = async (headers) => {
  // Get the authentication token 
  const token = await authProvider.getAccessToken();
  console.log((token.accessToken));
  return {
    headers: {
      ...headers,
      authorization: token.accessToken ? `Bearer ${token.accessToken}` : "",
    }
  } 
};