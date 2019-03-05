<template>
  <div>
    <h1>{{ msg }}</h1>
    <b-table striped hover :fields="table.fields" :items="table.items">
      <template slot="actions" slot-scope="row">
        <b-button :to="{ name: 'pas', params: { id: row.item.instance }}" size="sm">
          Details
        </b-button>
      </template>
    </b-table>
  </div>
</template>

<script>
export default {
  name: 'pases',
  data () {
    return {
      msg: 'Pas List',
      table: {
        fields: [
            { key: 'instance', label: 'Instance Name', sortable: true, 'class': 'text-center' },
            { key: 'actions', label: 'Actions' }
        ],
        items: []
      },
    }
  },
  mounted () {
    this.$http.get('/pas/').then(res => {
      this.table.items = res.data
    }, error => {
      this.$log.error(error)
    })
  }
}
</script>