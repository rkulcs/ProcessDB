export class UserDetails {
  static TOKEN = 'token'
  static USER = 'user'

  static getLoggedInUser() {
    return localStorage.getItem(this.USER)
  }

  static setLoggedInUserAndToken(user, token) {
    localStorage.setItem(this.USER, user)
    localStorage.setItem(this.TOKEN, token)
  }

  static getUserToken() {
    return localStorage.getItem(this.TOKEN)
  }

  static clearUserDetails() {
    localStorage.removeItem(this.USER)
    localStorage.removeItem(this.TOKEN)
  }
}

export default UserDetails
