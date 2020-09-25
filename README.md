# Pizzeria-APIs
Implement an API that represents the operations of a Pizzeria. You should accept and fulfill customer orders for pizza, noting the size of the pizza and whether the order is urgent. You should notify customers when an order succeeds or fails. You should allow customers to cancel open orders.
- Only accept orders for pizza whose size is between 4 and 16.
- Ensure the total number of open orders never exceeds 25, and the sum total pizza size for open orders never exceeds 250. Reject new orders that would breach these limits.
- Give priority to urgent orders.
- The operations of the Pizzeria must be concurrent. For example, customers must be able to place new orders while current orders are working, and the Pizzeria must be able to bake and deliver multiple orders at the same time.
