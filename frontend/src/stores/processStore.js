import { defineStore, mapState } from 'pinia'
import { Process } from '../entities/Process'
import { toRaw } from 'vue'
import axios from 'axios'

export const processStore = defineStore('processes', {
  state: () => {
    return {
      processes: []
    }
  },

  actions: {
    getAll() {
      (async () => {
        this.processes = await axios({
          method: 'GET',
          url: `${import.meta.env.VITE_BACKEND_URL}/processes/`,
          headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
          }
        }).then(response => response.data)
      })()
    },
    get(id) {
      return toRaw(this.processes.filter(p => p.id === id)[0])
    }
  },
},
{
  persist: true
}
)

export default processStore
