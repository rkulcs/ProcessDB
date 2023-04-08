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
      return (async () => {
        let processJson = await axios({
          method: 'GET',
          url: `${import.meta.env.VITE_BACKEND_URL}/processes/${id}`,
          headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
          }
        })
        .then(response => response.data)

        let process = Process.emptyProcess()
        process.copyJsonValues(processJson)

        return process
      })()
    },
    add(process) {
      return axios({
        method: 'POST',
        url: `${import.meta.env.VITE_BACKEND_URL}/processes/add`,
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        data: process
      })
      .then(true)
      .catch(false)
    },
    edit(process) {
      return axios({
        method: 'POST',
        url: `${import.meta.env.VITE_BACKEND_URL}/processes/${process.id}/update`,
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      .then(true)
      .catch(false)
    },
    delete(process) {
      return axios({
        method: 'DELETE',
        url: `${import.meta.env.VITE_BACKEND_URL}/processes/${process.id}/delete`,
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      .then(true)
      .catch(false)
    },
    addComment(processId, comment) {
      return axios({
        method: 'POST',
        url: `${import.meta.env.VITE_BACKEND_URL}/processes/${processId}/comments/add`,
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        data: comment.toSimplifiedJSON()
      })
      .then(true)
      .catch(false)
    }
  },
}
)

export default processStore
