<template id="app-header-template">
  <div class="container">
    <div class="jumbotron">
      <img :src = "require(`@/assets/${headerImage}.png`)" width="180px" height="100px"/>
      <h1>{{headerText}}</h1>
      <Home/>
    </div>
  </div>
</template>

<script>
  import Home from '@/components/Home'
export default {
  name: 'app-header',
  props: {
    headerImage: {
      default: 'logo'

    },
    headerText: {
      default: 'Welcome to the Garage'

    },
  },
  components: {
    Home
  }
}
</script>

<style>
</style>
