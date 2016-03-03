This program tries to read Google Cardboard's current_device_params file, and
display the parameters to the user. Ideally, it would also allow the user to adjust
the interlens distance, as this is also a sort of proxy for interpupillary distance,
which varies widely between users.

It's based off of Cardboard Profile Dumper:
https://github.com/TheOnlyJoey/CardboardProfileDumper

and the CardboardDevice.proto file from
https://github.com/google/wwgc/blob/master/www/CardboardDevice.proto

Current problems:
--Has to skip the first 7 bytes of the file in order to read correctly.  Since
  I don't know what these bytes signify, this prevents editing/writing the file.
--Doesn't seem to show the has_magnet or primary_button fields from the
  protocol buffer. Oddly, when I viewed a sample file in a hex editor, I didn't
  see these values either; according to their types and position numbers, and
  the encoding scheme described at
  https://developers.google.com/protocol-buffers/docs/encoding#types
  I think they should be prefixed by 50 and 60, respectively, but I don't see
  those values in the file. I really need to print out the file and annotate
  it, though.