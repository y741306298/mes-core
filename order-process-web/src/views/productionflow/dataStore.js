 const ORDER_KEY = 'productionflow_order_list'
 const FLOW_KEY = 'productionflow_flow_list'

 function getStorage() {
   if (typeof window === 'undefined' || !window.localStorage) {
     return null
   }
   return window.localStorage
 }

 function safeParse(raw, fallback) {
   if (!raw) return fallback
   try {
     const parsed = JSON.parse(raw)
     return Array.isArray(fallback) ? (Array.isArray(parsed) ? parsed : fallback) : parsed
   } catch (e) {
     return fallback
   }
 }

 export function loadOrders(fallback = []) {
   const storage = getStorage()
   if (!storage) return JSON.parse(JSON.stringify(fallback))
   const raw = storage.getItem(ORDER_KEY)
   const result = safeParse(raw, fallback)
   return JSON.parse(JSON.stringify(result))
 }

 export function saveOrders(list) {
   const storage = getStorage()
   if (!storage) return
   storage.setItem(ORDER_KEY, JSON.stringify(list || []))
 }

 export function loadFlows(fallback = []) {
   const storage = getStorage()
   if (!storage) return JSON.parse(JSON.stringify(fallback))
   const raw = storage.getItem(FLOW_KEY)
   const result = safeParse(raw, fallback)
   return JSON.parse(JSON.stringify(result))
 }

 export function saveFlows(list) {
   const storage = getStorage()
   if (!storage) return
   storage.setItem(FLOW_KEY, JSON.stringify(list || []))
 }
