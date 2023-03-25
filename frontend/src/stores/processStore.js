import { defineStore, mapState } from 'pinia'
import { Process } from '../entities/Process'
import axios from 'axios'

export const processStore = defineStore('processes', {
  state: () => {
    return {
      processes: []
    }
  },

  actions: {
    getAll() {
      axios({
        method: 'GET',
        url: `${import.meta.env.VITE_BACKEND_URL}/processes/`,
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
        }
      }).then(response => {
        this.processes = response.data
      })
    }
  }
})

export default processStore
