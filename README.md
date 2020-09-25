# Pizzeria-APIs
Implementing an API that represents the operations of a Pizzeria. 
1.Accept and fulfill customer orders for pizza, noting the size of the pizza and whether the order is urgent. 
2. Notify customers when an order succeeds or fails. 
3. Allow customers to cancel open orders.
Conditions : 
- Only accept orders with pizza size between 4 and 16.
- Ensure the total number of open orders never exceeds 25, and the sum total pizza size for open orders never exceeds 250. Reject new orders that would breach these limits.
- Give priority to urgent orders.
- The operations of the Pizzeria must be concurrent. For example, customers must be able to place new orders while current orders are working, and the Pizzeria must be able to bake and deliver multiple orders at the same time.
