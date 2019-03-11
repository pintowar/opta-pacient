<template>
  <b-container fluid>
    <b-row>
      <b-col cols="12">
        <h1>{{ msg }}</h1>
        <b-alert variant="danger" dismissible fade
                  :show="errorAlert" @dismissed="errorAlert=false" >
                  {{errorMsg}}
        </b-alert>
      </b-col>
    </b-row>
    <b-row>
      <b-col cols="12">
        <b-tabs content-class="mt-3">
          <b-tab title="Visual" active>
            <timeline :items="items" :groups="groups"/>
          </b-tab>
          <b-tab title="Json Editor">
              <vue-json-editor v-model="pas" :show-btns="false"/>
          </b-tab>
        </b-tabs>
      </b-col>
    </b-row>
    <b-row>
      <b-col cols="12">
        <b-button type="submit" variant="primary" @click="onSubmit">Submit</b-button>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import vueJsonEditor from 'vue-json-editor'
import Timeline from '@/components/Timeline'
import moment from 'moment'

export default {
  name: 'pas',
  components: {
      vueJsonEditor, Timeline
  },
  data () {
    return {
      msg: 'Pas Edit',
      pas: {},
      rooms: {},
      beds: {},
      nights: {},
      patients: {},
      errorAlert: false,
      errorMsg: '',
    }
  },
  computed: {
    groups () {
      // return (this.pas.departmentList || []).flatMap(d => d.roomList.flatMap(r => {
      //     const room = this.rooms[r]
      //     return room.bedList.flatMap(b => {
      //       const bed = this.beds[b]
      //       const desc = `${d.name} | ${room.name} | ${bed.id}`
      //       return {id: bed.id, content: desc, value: bed.id}
      //     })
      //   })
      // )
      return [{id: 0, content: 'Unassigned', value: 0}]
    },
    items () {
      const beginning = [2019, 1, 1]
      // debugger
      return (this.pas.admissionPartList || []).map(admission => {
        const start = moment(beginning).add(this.nights[admission.firstNight].index, 'days')
        const end = moment(beginning).add(this.nights[admission.lastNight].index + 1, 'days')
        const patient = this.patients[admission.patient]

        return { id: `trip-${admission.id}`, group: 0, start: start.toDate(), end: end.toDate(), 
              className: 'trip', content: patient.name }
      })
    }
  },
  mounted () {
    const id = this.$route.params.id
    this.$http.get(`/pas/${id}`).then(res => {
      this.pas = res.data
      this.rooms = this.identifications(this.pas.roomList)
      this.beds = this.identifications(this.pas.bedList)
      this.patients = this.identifications(this.pas.patientList)
      this.nights = this.identifications(this.pas.nightList)
    }, error => {
      this.$log.error(error)
      this.errorAlert = true
      this.errorMsg = error.message
    })
  },
  methods: {
    onSubmit () {
      this.$http.put(`/pas/`, this.pas).then(res => {
        this.pas = res.data
        this.rooms = this.identifications(this.pas.roomList)
        this.beds = this.identifications(this.pas.bedList)
        this.patients = this.identifications(this.pas.patientList)
        this.nights = this.identifications(this.pas.nightList)
      }, error => {
        this.$log.error(error)
        this.errorAlert = true
        this.errorMsg = error.message
      })
    }, 
    identifications (list) {
      let obj = {}
      list.forEach(e => obj[e['@id']] = e);
      return obj
    },
  }
}
</script>