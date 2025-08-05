package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.RichText;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LIstBlocksDeserializationTestBaseBlockDeserializationTest extends BaseBlockDeserializationTest {


    @Test
    void testToDoListItemUnchecked() {
        boolean found = false;
        for (Block block : response.getResults()) {
            if ("22dc5b96-8ec4-80b6-aeab-ed4da5e9ce31".equals(block.getId())) {
                found = true;
                ToDoBlock todo = (ToDoBlock) block;
                assertEquals("block", todo.getObject());
                assertEquals("to_do", todo.getType());
                assertFalse(todo.hasChildren());
                assertFalse(todo.isArchived());
                assertFalse(todo.isInTrash());
                assertParent(todo, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
                assertEquals("2025-07-11T18:00:00.000Z", todo.getCreatedTime());
                assertEquals("2025-07-11T18:00:00.000Z", todo.getLastEditedTime());
                assertNotNull(todo.getCreatedBy());
                assertEquals("user", todo.getCreatedBy().getObject());
                assertEquals("abaaea82-caae-ddsf-eads-888018852f04", todo.getCreatedBy().getId());
                assertNotNull(todo.getLastEditedBy());
                assertEquals("user", todo.getLastEditedBy().getObject());
                assertEquals("abaaea82-caae-ddsf-eads-888018852f04", todo.getLastEditedBy().getId());
                assertNotNull(todo.getToDo());
                assertEquals("default", todo.getToDo().getColor());
                assertFalse(todo.getToDo().getChecked());
                assertNotNull(todo.getToDo().getRichText());
                assertEquals(1, todo.getToDo().getRichText().size());
                RichText text = todo.getToDo().getRichText().get(0);
                assertEquals("text", text.getType());
                assertNotNull(text.getText());
                assertEquals("Unckecked", text.getText().getContent());
                assertNull(text.getText().getLink());
                assertNotNull(text.getAnnotations());
                assertFalse(text.getAnnotations().isBold());
                assertFalse(text.getAnnotations().isItalic());
                assertFalse(text.getAnnotations().isStrikethrough());
                assertFalse(text.getAnnotations().isUnderline());
                assertFalse(text.getAnnotations().isCode());
                assertEquals("default", text.getAnnotations().getColor());
                assertEquals("Unckecked", text.getPlainText());
                assertNull(text.getHref());
            }
        }
        assertTrue(found);
    }

    @Test
    void testToDoListItemChecked() {
        boolean found = false;
        for (Block block : response.getResults()) {
            if ("22dc5b96-8ec4-803a-9edd-c3968ed34e59".equals(block.getId())) {
                found = true;
                ToDoBlock todo = (ToDoBlock) block;
                assertEquals("block", todo.getObject());
                assertEquals("to_do", todo.getType());
                assertFalse(todo.hasChildren());
                assertFalse(todo.isArchived());
                assertFalse(todo.isInTrash());
                assertParent(todo, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
                assertEquals("2025-07-11T18:00:00.000Z", todo.getCreatedTime());
                assertEquals("2025-07-11T18:00:00.000Z", todo.getLastEditedTime());
                assertNotNull(todo.getCreatedBy());
                assertEquals("user", todo.getCreatedBy().getObject());
                assertEquals("abaaea82-caae-ddsf-eads-888018852f04", todo.getCreatedBy().getId());
                assertNotNull(todo.getLastEditedBy());
                assertEquals("user", todo.getLastEditedBy().getObject());
                assertEquals("abaaea82-caae-ddsf-eads-888018852f04", todo.getLastEditedBy().getId());
                assertNotNull(todo.getToDo());
                assertEquals("default", todo.getToDo().getColor());
                assertTrue(todo.getToDo().getChecked());
                assertNotNull(todo.getToDo().getRichText());
                assertEquals(1, todo.getToDo().getRichText().size());
                RichText text = todo.getToDo().getRichText().get(0);
                assertEquals("text", text.getType());
                assertNotNull(text.getText());
                assertEquals("Checked", text.getText().getContent());
                assertNull(text.getText().getLink());
                assertNotNull(text.getAnnotations());
                assertFalse(text.getAnnotations().isBold());
                assertFalse(text.getAnnotations().isItalic());
                assertFalse(text.getAnnotations().isStrikethrough());
                assertFalse(text.getAnnotations().isUnderline());
                assertFalse(text.getAnnotations().isCode());
                assertEquals("default", text.getAnnotations().getColor());
                assertEquals("Checked", text.getPlainText());
                assertNull(text.getHref());
            }
        }
        assertTrue(found);
    }


    @Test
    void testNumberedListItem() {
        Block b = findBlockById("22dc5b96-8ec4-8001-8a0a-d7d7ad37d3d6");
        assertNotNull(b);

        NumberedListItemBlock n = (NumberedListItemBlock) b;
        assertEquals("block", n.getObject());
        assertEquals("numbered_list_item", n.getType());
        assertFalse(n.hasChildren());
        assertFalse(n.isArchived());
        assertFalse(n.isInTrash());
        assertParent(n, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
        assertEquals("2025-07-11T18:02:00.000Z", n.getCreatedTime());
        assertEquals("2025-07-11T18:02:00.000Z", n.getLastEditedTime());
        assertNotNull(n.getCreatedBy());
        assertEquals("user", n.getCreatedBy().getObject());
        assertEquals("abaaea82-caae-ddsf-eads-888018852f04", n.getCreatedBy().getId());
        assertNotNull(n.getLastEditedBy());
        assertEquals("user", n.getLastEditedBy().getObject());
        assertEquals("abaaea82-caae-ddsf-eads-888018852f04", n.getLastEditedBy().getId());
        assertNotNull(n.getNumberedListItem());
        assertEquals("default", n.getNumberedListItem().getColor());
        assertNotNull(n.getNumberedListItem().getRichText());
        assertEquals(4, n.getNumberedListItem().getRichText().size());

        RichText t0 = n.getNumberedListItem().getRichText().get(0);
        assertEquals("text", t0.getType());
        assertNotNull(t0.getText());
        assertEquals("Numbered ", t0.getText().getContent());
        assertNull(t0.getText().getLink());
        assertEquals("Numbered ", t0.getPlainText());
        assertNull(t0.getHref());
        assertNotNull(t0.getAnnotations());
        assertFalse(t0.getAnnotations().isBold());
        assertFalse(t0.getAnnotations().isItalic());
        assertFalse(t0.getAnnotations().isStrikethrough());
        assertFalse(t0.getAnnotations().isUnderline());
        assertFalse(t0.getAnnotations().isCode());
        assertEquals("default", t0.getAnnotations().getColor());

        RichText t1 = n.getNumberedListItem().getRichText().get(1);
        assertEquals("text", t1.getType());
        assertNotNull(t1.getText());
        assertEquals("list", t1.getText().getContent());
        assertNull(t1.getText().getLink());
        assertEquals("list", t1.getPlainText());
        assertNull(t1.getHref());
        assertNotNull(t1.getAnnotations());
        assertTrue(t1.getAnnotations().isBold());
        assertFalse(t1.getAnnotations().isItalic());
        assertFalse(t1.getAnnotations().isStrikethrough());
        assertFalse(t1.getAnnotations().isUnderline());
        assertFalse(t1.getAnnotations().isCode());
        assertEquals("green_background", t1.getAnnotations().getColor());

        RichText t2 = n.getNumberedListItem().getRichText().get(2);
        assertEquals("text", t2.getType());
        assertNotNull(t2.getText());
        assertEquals(" 1 with ", t2.getText().getContent());
        assertNull(t2.getText().getLink());
        assertEquals(" 1 with ", t2.getPlainText());
        assertNull(t2.getHref());
        assertNotNull(t2.getAnnotations());
        assertFalse(t2.getAnnotations().isBold());
        assertFalse(t2.getAnnotations().isItalic());
        assertFalse(t2.getAnnotations().isStrikethrough());
        assertFalse(t2.getAnnotations().isUnderline());
        assertFalse(t2.getAnnotations().isCode());
        assertEquals("default", t2.getAnnotations().getColor());

        RichText t3 = n.getNumberedListItem().getRichText().get(3);
        assertEquals("text", t3.getType());
        assertNotNull(t3.getText());
        assertEquals("rich text", t3.getText().getContent());
        assertNull(t3.getText().getLink());
        assertEquals("rich text", t3.getPlainText());
        assertNull(t3.getHref());
        assertNotNull(t3.getAnnotations());
        assertFalse(t3.getAnnotations().isBold());
        assertFalse(t3.getAnnotations().isItalic());
        assertFalse(t3.getAnnotations().isStrikethrough());
        assertFalse(t3.getAnnotations().isUnderline());
        assertFalse(t3.getAnnotations().isCode());
        assertEquals("blue", t3.getAnnotations().getColor());

    }

    @Test
    void testBulletedListItem() {
        Block block = findBlockById("22dc5b96-8ec4-8066-a751-ccc4b922d045");
        assertNotNull(block);

        BulletedListItemBlock b = (BulletedListItemBlock) block;
        assertEquals("block", b.getObject());
        assertEquals("bulleted_list_item", b.getType());
        assertFalse(b.hasChildren());
        assertFalse(b.isArchived());
        assertFalse(b.isInTrash());
        assertParent(b, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
        assertEquals("2025-07-11T18:00:00.000Z", b.getCreatedTime());
        assertEquals("2025-07-11T18:00:00.000Z", b.getLastEditedTime());
        assertNotNull(b.getCreatedBy());
        assertEquals("user", b.getCreatedBy().getObject());
        assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getCreatedBy().getId());
        assertNotNull(b.getLastEditedBy());
        assertEquals("user", b.getLastEditedBy().getObject());
        assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getLastEditedBy().getId());
        assertNotNull(b.getBulletedListItem());
        assertEquals("default", b.getBulletedListItem().getColor());
        assertNotNull(b.getBulletedListItem().getRichText());
        assertEquals(1, b.getBulletedListItem().getRichText().size());
        RichText text = b.getBulletedListItem().getRichText().get(0);
        assertEquals("text", text.getType());
        assertNotNull(text.getText());
        assertEquals("bulleted list 1", text.getText().getContent());
        assertNull(text.getText().getLink());
        assertNotNull(text.getAnnotations());
        assertFalse(text.getAnnotations().isBold());
        assertFalse(text.getAnnotations().isItalic());
        assertFalse(text.getAnnotations().isStrikethrough());
        assertFalse(text.getAnnotations().isUnderline());
        assertFalse(text.getAnnotations().isCode());
        assertEquals("default", text.getAnnotations().getColor());
        assertEquals("bulleted list 1", text.getPlainText());
        assertNull(text.getHref());
    }

    @Test
    void testToggleListItem() {
        Block block = findBlockById("22dc5b96-8ec4-8013-b2c6-daaa49270415");
        assertNotNull(block);

        ToggleBlock t = (ToggleBlock) block;
        assertEquals("block", t.getObject());
        assertEquals("toggle", t.getType());
        assertTrue(t.hasChildren());
        assertFalse(t.isArchived());
        assertFalse(t.isInTrash());
        assertParent(t, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
        assertEquals("2025-07-11T18:01:00.000Z", t.getCreatedTime());
        assertEquals("2025-07-11T18:01:00.000Z", t.getLastEditedTime());
        assertNotNull(t.getCreatedBy());
        assertEquals("user", t.getCreatedBy().getObject());
        assertEquals("abaaea82-caae-ddsf-eads-888018852f04", t.getCreatedBy().getId());
        assertNotNull(t.getLastEditedBy());
        assertEquals("user", t.getLastEditedBy().getObject());
        assertEquals("abaaea82-caae-ddsf-eads-888018852f04", t.getLastEditedBy().getId());
        assertNotNull(t.getToggle());
        assertEquals("default", t.getToggle().getColor());
        assertNotNull(t.getToggle().getRichText());
        assertEquals(1, t.getToggle().getRichText().size());
        RichText text = t.getToggle().getRichText().get(0);
        assertEquals("text", text.getType());
        assertNotNull(text.getText());
        assertEquals("Regular toggle", text.getText().getContent());
    }

}
