# AndroidGroupList


HI, JJengine provide easy way to solve simple problem.<br>

GroupList let you adding multi view types into listview in a easy way.
If you have any problem using in ScrollView with nested Listview, you may try GroupList.

##Usage
step1. Copy AbsGroupAdapter.java into your project.<br>
step2. Extend AbsGroupAdapter and init it <br>
```Java
        ListView listView = (ListView) findViewById(R.id.listview);
        final GroupAdapter adapter = new GroupAdapter(getApplicationContext()); //Extend AbsGroupAdapter
        adapter.bindListView(listView);
        listView.setAdapter(adapter);

        adapter.addGroup(10); //you can add a group
        final ImageView image = new ImageView(getApplicationContext());
        image.setImageResource(R.drawable.image);
        adapter.addStableView(image); //or you can add a view
        adapter.addGroup(10); // add a group
        adapter.addGroup(10); // add a group
        adapter.notifyDataSetChanged();
```
##Demo
Demo is MainActivity.java<br>

