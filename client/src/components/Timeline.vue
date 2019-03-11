<template>
    <div ref="visualization"></div>
</template>

<script>
import { DataSet, DataView, Timeline } from 'vis'
import 'vis/dist/vis.css'
export default {
    name: 'timeline',
    props: {
        groups: {
            type: [Array, DataSet, DataView]
        },
        items: {
            type: [Array, DataSet, DataView]
        }
    },
    data () {
        return {
            options: {
                showMajorLabels: true,
                showCurrentTime: false,
                zoomMin: 1000000,
                groupOrder: function (a, b) { return a.value - b.value; },
                format: {
                    minorLabels: { minute: 'h:mma', hour: 'ha' }
                },
                orientation: { axis: 'top' },
                margin: {item: 1}
            }
        }
    },
    computed: {
        visData () {
            return { items: this.items, groups: this.groups }
        }
    },
    mounted() {
        const container = this.$refs.visualization
        this.timeline = new Timeline(container, this.visData.items, this.visData.groups, this.options)
    },
    watch: {
        visData () {
            const container = this.$refs.visualization
            this.timeline.destroy()
            this.timeline = new Timeline(container, this.visData.items, this.visData.groups, this.options)
        }
    },
    beforeDestroy() {
        this.timeline.destroy()
    }
}
</script>

<style>

</style>