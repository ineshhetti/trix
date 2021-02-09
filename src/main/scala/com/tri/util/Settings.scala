package com.tri.util

import akka.stream.alpakka.google.firebase.fcm.FcmSettings

object Settings {
  val privateKey ="""-----BEGIN PRIVATE KEY-----
    |MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCQdGfUSZee0vhJ
    |YL+qf6Nci03hxzYsvkTAmAj+kg79TVii7fdWc2gh9WQSyBE6841mQfEYrjtlc3Or
    |FefwD1qrhvrTKyPi1BzEomaukoCH2QlahdDfInCaz9hthRev+FyuEHwdo5pPcj8q
    |6eVG6mPDL4dF+fIKNehxVDZr/SjRAvAkntsh4ZOc5UZvkSqxHur313RrB+59FFhb
    |arxiERMorZc9UB/fApkjgIij4An/3REri+ZhBdx3xYQJGCtt0jQWsaPDS12EZj29
    |z2V0bej4+2dwp1DGajYm7O4+4vfMMMCE4VHYnF7r8XKcH5sEmTjq8QKwZfXkDCwO
    |+1Rtx24JAgMBAAECggEACSXsEOY/tUHWUjxPqw4mA/V4/05IxKTgkQNX13pbu2gn
    |b7sLBcUtgDAMkEFuEOoNZ39P09Ct54b8GWa732izhOcZkZ+66DGnaD8CKnZR8Dh7
    |FongsE7bq3wz8DykY0Mi6G2YFJ7wpe0d6btyBw+PNZRYGGMDtGiVUb8D6ssXp4PU
    |GwpYBC0MvxdF27+j3pKLWQFvJGCE9+8lv6GXb8A4sNLqG97/3hceAMGiWIWfZHi2
    |B5kE1WfSd5bxuUf3l+THtf+dEvxtFJWgDYhejvagoQ9d76O4X/wKitJ1PpqoaoAx
    |Nq1BAMhvUyC9fouEonCT6MkjUSaiq7S9SPbNnjYksQKBgQDKKRZY2RFXiI21GE6X
    |qT95a9ie9hhdrS/MXTThUtCVjbflvs4SJrID/r2zLCHl4FkZAeG70f7PUTAw3JGv
    |XfUpYjxZBs9QNfxSWMgmC7UqvRIC6+Rho5z4BNMlwpHLIitM6rzNgJflTuhbHgWd
    |LezBPPFeYJgyzI5wG5R7n4S2sQKBgQC27QyKq/4fX6VX1la6pdX+7rTcfBK5y1wY
    |yUZQDDJYm/aZfYOyUYA7trSoA7b15RzKyPAb9dkhP7Fot7Rv52iM63kig5VsBlmI
    |4UwruE9LHiCstVNj+nG8rjmTX9GV8bIjTxwITPr6KyUaV9nJKlqwBLgrj4vlyCLa
    |8NQt7roy2QKBgFmOtCiuGQGnK+fjGcYUI1t+DFv3d7ngJEGrtOZxEph+TM6Jpa7i
    |14/vhUhfr7Cvn6j1bXQQRoq/U9MBk8SVoiswKr3hx2uoTtyIGjrIivyg/96tc+JR
    |YhN5x7R9qqrLu1T61KNRaXkbaNIcza3hD3ZXsjH2JtayShOeFDxj4Z4xAoGAJF6B
    |DdkgT1CO6SXeXIG4SFVhdCze9D2wc+Ugq62hHOj1YJMJ6hw2KKksxfLnH4CL9u14
    |f7ajku688RsFLvKNYOc4UFplDPM+Pe69XDvq1/6HdI8go1IZ5u+k1t5IJ03EoyyJ
    |YEUd6TtGxzQMQui+2xw1GcQShn8CHxGevfsm3hECgYAO6QLmCQqFFX/Azeka1JJg
    |tlplZ05D0AZ3AgRHUwsyibYCanpwct82okVfhaoR+rYLLp+E8zkuyb1jJ4bcyZNr
    |8XZmxpeXgMNC7gEomCS4UE/+b2Swl+HsM5Ton8phaxTbuJUPPntm3U2an15tDgZK
    |X1CmZdciuBIJmqW1rh7iaA==
    |-----END PRIVATE KEY-----
    |""".stripMargin
  val clientEmail = "firebase-adminsdk-qn12k@trix-bc458.iam.gserviceaccount.com"
  val projectId = "trix-bc458"
  val fcmConfig = FcmSettings(clientEmail, privateKey, projectId)
}
