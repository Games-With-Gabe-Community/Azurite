package ai.statemachine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 15.07.2021
 * @since 15.07.2021
 */
public class StateMachineTest {

    private StateMachine testMachine1;
    private StateMachine testMachine2;

    private int machine1Counter;
    private String logString;

    @Before
    public void setUp() throws Exception {
        this.testMachine1 = new StateMachine();
        this.testMachine2 = new StateMachine();

        this.testMachine1.addState("idle", new State() {
            @Override
            public void enterState() {
                logString += "ENTER:idle,";
            }

            @Override
            public void updateState(float dt) {
                machine1Counter++;
                logString += "UPDATE:idle,";
            }

            @Override
            public void exitState() {
                logString += "EXIT:idle,";
            }
        });
        this.testMachine1.addState("other", new State() {
            @Override
            public void enterState() {
                logString += "ENTER:other,";
            }

            @Override
            public void updateState(float dt) {
                machine1Counter--;
                logString += "UPDATE:other,";
            }

            @Override
            public void exitState() {
                logString += "EXIT:other,";
            }
        });
        this.testMachine1.addTransition("idle", "other", state -> true);
        this.testMachine1.addTransition("other", "idle", state -> true);

        this.testMachine2.addState("idle", new State() {
            @Override
            public void enterState() {

            }

            @Override
            public void updateState(float dt) {

            }

            @Override
            public void exitState() {

            }
        });
        this.testMachine2.addState("other", new State() {
            @Override
            public void enterState() {

            }

            @Override
            public void updateState(float dt) {

            }

            @Override
            public void exitState() {

            }
        });
        //only one transition to test validate
        this.testMachine2.addTransition("idle", "other", state -> true);
        this.testMachine1.startMachine("idle");
        this.testMachine2.startMachine("idle");
    }

    @Test
    public void hasTransition() {
        Assert.assertTrue(this.testMachine1.hasTransition("idle", "other"));
        Assert.assertTrue(this.testMachine1.hasTransition("other", "idle"));
        Assert.assertFalse(this.testMachine1.hasTransition("unknown", "idle"));
        Assert.assertFalse(this.testMachine1.hasTransition("idle", "unknown"));
        Assert.assertFalse(this.testMachine1.hasTransition("unknown", "other_unknown"));

        Assert.assertTrue(this.testMachine1.hasTransition("idle"));
        Assert.assertTrue(this.testMachine1.hasTransition("other"));
        Assert.assertFalse(this.testMachine1.hasTransition("unknown"));

        Assert.assertTrue(this.testMachine2.hasTransition("idle", "other"));
        //this transition should be missing
        Assert.assertFalse(this.testMachine2.hasTransition("other", "idle"));
        Assert.assertFalse(this.testMachine2.hasTransition("unknown", "idle"));
        Assert.assertFalse(this.testMachine2.hasTransition("idle", "unknown"));
        Assert.assertFalse(this.testMachine2.hasTransition("unknown", "other_unknown"));

        Assert.assertTrue(this.testMachine2.hasTransition("idle"));
        //this should have no transitions
        Assert.assertFalse(this.testMachine2.hasTransition("other"));
        Assert.assertFalse(this.testMachine2.hasTransition("unknown"));
    }

    @Test
    public void canTransitionTo() {
        Assert.assertTrue(this.testMachine1.canTransitionTo("idle"));
        Assert.assertTrue(this.testMachine1.canTransitionTo("other"));
        Assert.assertFalse(this.testMachine1.canTransitionTo("unknown"));

        Assert.assertFalse(this.testMachine2.canTransitionTo("idle"));
        Assert.assertTrue(this.testMachine2.canTransitionTo("other"));
        Assert.assertFalse(this.testMachine2.canTransitionTo("unknown"));
    }

    @Test
    public void hasState() {
        Assert.assertTrue(this.testMachine1.hasState("idle"));
        Assert.assertTrue(this.testMachine1.hasState("other"));
        Assert.assertFalse(this.testMachine1.hasState("unknown"));

        Assert.assertTrue(this.testMachine2.hasState("idle"));
        Assert.assertTrue(this.testMachine2.hasState("other"));
        Assert.assertFalse(this.testMachine2.hasState("unknown"));
    }

    @Test
    public void addState() {
        Assert.assertFalse(this.testMachine1.hasState("no_name"));
        this.testMachine1.addState("no_name", new State() {
            @Override
            public void enterState() {

            }

            @Override
            public void updateState(float dt) {

            }

            @Override
            public void exitState() {

            }
        });
        Assert.assertTrue(this.testMachine1.hasState("no_name"));
    }

    @Test
    public void addTransition() {
        Assert.assertFalse(this.testMachine1.addTransition("idle", "newstate", state -> Math.random() < 0.3f));
        Assert.assertFalse(this.testMachine1.hasTransition("idle", "newstate"));
        this.testMachine1.addState("newstate", new State() {
            @Override
            public void enterState() {

            }

            @Override
            public void updateState(float dt) {

            }

            @Override
            public void exitState() {

            }
        });
        Assert.assertTrue(this.testMachine1.addTransition("idle", "newstate", state -> Math.random() < 0.3f));
        Assert.assertTrue(this.testMachine1.hasTransition("idle", "newstate"));
        Assert.assertTrue(this.testMachine1.doTransition("newstate"));
        Assert.assertEquals("newstate", this.testMachine1.getCurrentStateName());
        Assert.assertTrue(this.testMachine1.doTransition("idle"));
        Assert.assertEquals("idle", this.testMachine1.getCurrentStateName());
    }

    @Test
    public void startMachine() {
        //machines has already started, therefore, it should fail
        Assert.assertFalse(this.testMachine1.startMachine("idle"));
        Assert.assertFalse(this.testMachine2.startMachine("idle"));

        StateMachine machine = new StateMachine();
        machine.addState("test", new State() {
            @Override
            public void enterState() {

            }

            @Override
            public void updateState(float dt) {

            }

            @Override
            public void exitState() {

            }
        });

        Assert.assertTrue(machine.startMachine("test"));
        //start should only work once
        Assert.assertFalse(machine.startMachine("test"));
    }

    @Test
    public void doTransition() {
        Assert.assertEquals("idle", this.testMachine1.getCurrentStateName());
        Assert.assertTrue(this.testMachine1.doTransition("other"));
        Assert.assertEquals("other", this.testMachine1.getCurrentStateName());
        Assert.assertTrue(this.testMachine1.doTransition("idle"));
        Assert.assertEquals("idle", this.testMachine1.getCurrentStateName());
        Assert.assertFalse(this.testMachine1.doTransition("anyUnknownState"));
        Assert.assertEquals("idle", this.testMachine1.getCurrentStateName());
    }

    @Test
    public void validate() {
        //machine1 has no deadlock nodes nor orphaned nodes
        Assert.assertTrue(this.testMachine1.validate());
        //machine2 has a deadlock node: other
        Assert.assertFalse(this.testMachine2.validate());
    }

    @Test
    public void update() {
        logString = "";
        machine1Counter = 0;
        //run update with any number
        this.testMachine1.update(1.0f);
        Assert.assertFalse(logString.isEmpty());
        Assert.assertEquals(-1, machine1Counter);
        Assert.assertEquals("EXIT:idle,ENTER:other,UPDATE:other,", logString);
        //run update again with any number
        this.testMachine1.update(1.0f);
        Assert.assertEquals(0, machine1Counter);
        Assert.assertEquals("EXIT:idle,ENTER:other,UPDATE:other,EXIT:other,ENTER:idle,UPDATE:idle,", logString);
    }
}