# comments
ALTER TABLE comments
    DROP FOREIGN KEY comments_ibfk_1;
ALTER TABLE comments
    DROP FOREIGN KEY comments_ibfk_2;
ALTER TABLE comments
    ADD CONSTRAINT comments_ibfk_1 FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE;
ALTER TABLE comments
    ADD CONSTRAINT comments_ibfk_2 FOREIGN KEY (member_id) REFERENCES members(member_id);

# orders
ALTER TABLE orders
    DROP FOREIGN KEY orders_ibfk_1;
ALTER TABLE comments
    ADD CONSTRAINT orders_ibfk_1 FOREIGN KEY (member_id) REFERENCES members(member_id);

# items
ALTER TABLE items
    DROP FOREIGN KEY items_ibfk_1;
ALTER TABLE items
    DROP FOREIGN KEY items_ibfk_2;
ALTER TABLE items
    DROP FOREIGN KEY items_ibfk_3;
ALTER TABLE items
    ADD CONSTRAINT items_ibfk_1 FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE;
ALTER TABLE items
    ADD CONSTRAINT items_ibfk_2 FOREIGN KEY (member_id) REFERENCES members(member_id);
ALTER TABLE items
    ADD CONSTRAINT items_ibfk_3 FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE;
