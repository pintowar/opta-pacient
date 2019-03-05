<template>
  <div>
    <h1>{{ msg }}</h1>
    <b-tabs content-class="mt-3">
        <b-tab title="Json Editor" active>
            <b-alert variant="danger" dismissible fade
                :show="errorAlert" @dismissed="errorAlert=false" >
                {{errorMsg}}
                </b-alert>
            <b-form @submit="onSubmit">
                <vue-json-editor v-model="pas" :show-btns="false"/>
                <b-button type="submit" variant="primary">Submit</b-button>
            </b-form>
        </b-tab>
        <!-- <b-tab title="second">I'm the second tab content</b-tab> -->
    </b-tabs>
  </div>
</template>

<script>
import vueJsonEditor from 'vue-json-editor'

export default {
  name: 'pas',
  components: {
      vueJsonEditor
  },
  data () {
    return {
      msg: 'Pas Edit',
      pas: {},
      errorAlert: false,
      errorMsg: '',
    }
  },
  mounted () {
    const id = this.$route.params.id
    this.$http.get(`/pas/${id}`).then(res => {
      this.pas = res.data
    }, error => {
      this.$log.error(error)
      this.errorAlert = true
      this.errorMsg = error.message
    })
  },
  methods: {
    onSubmit (evt) {
      evt.preventDefault()
      this.$http.put(`/pas/`, this.pas).then(res => {
      this.pas = res.data
    }, error => {
      this.$log.error(error)
    })
    }
  }
}
</script>